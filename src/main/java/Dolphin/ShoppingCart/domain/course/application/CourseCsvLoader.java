package Dolphin.ShoppingCart.domain.course.application;

import Dolphin.ShoppingCart.domain.academic.entity.Professor;
import Dolphin.ShoppingCart.domain.academic.repository.ProfessorRepository;
import Dolphin.ShoppingCart.domain.course.entity.Course;
import Dolphin.ShoppingCart.domain.course.entity.CourseType;
import Dolphin.ShoppingCart.domain.course.entity.Teach;
import Dolphin.ShoppingCart.domain.course.entity.TeachInfo;
import Dolphin.ShoppingCart.domain.course.enums.DayOfWeekType;
import Dolphin.ShoppingCart.domain.course.enums.MajorType;
import Dolphin.ShoppingCart.domain.course.enums.SecondMajorType;
import Dolphin.ShoppingCart.domain.course.enums.SemesterType;
import Dolphin.ShoppingCart.domain.course.enums.TeachType;
import Dolphin.ShoppingCart.domain.course.repository.CourseRepository;
import Dolphin.ShoppingCart.domain.course.repository.CourseTypeRepository;
import Dolphin.ShoppingCart.domain.course.repository.TeachInfoRepository;
import Dolphin.ShoppingCart.domain.course.repository.TeachRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseCsvLoader {

    private final CourseRepository courseRepository;
    private final CourseTypeRepository courseTypeRepository;
    private final TeachRepository teachRepository;
    private final TeachInfoRepository teachInfoRepository;
    private final ProfessorRepository professorRepository;

    @Transactional
    public void load() throws Exception {

        if (teachRepository.count() > 0) {
            log.info("Teach 데이터가 존재해서 CSV 로딩 스킵");
            return;
        }

        ClassPathResource resource = new ClassPathResource("csv/25-2.csv");

        Map<String, Course> courseCache = new HashMap<>();

        try (Reader in = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(in);

            for (CSVRecord record : records) {
                String majorityRaw = record.get("majority").trim();
                String secondMajorRaw = record.get("secondMajor"); // null 가능
                String numberRaw = record.get("number").trim();
                String name = record.get("name").trim();
                String typeRaw = record.get("type"); // EL+, 영어, 영한혼합, 공백
                String className = record.get("className"); // null 가능
                String professorName = record.get("professorName").trim();
                String timeRaw = record.get("time");
                String creditRaw = record.get("credit");
                String maxCountRaw = record.get("maxCount");
                String scheduleRaw = record.get("dayOfTheWeek/startTime/endTime/classroom");
                String semesterRaw = record.get("semester").trim();
                String yearRaw = record.get("year").trim();
                String gradeRaw = record.get("grade"); // 전체/1학년/2학년/3학년/4학년

                MajorType majorType = mapMajorType(majorityRaw);
                Float time = timeRaw == null || timeRaw.isBlank() ? null : Float.parseFloat(timeRaw);
                Float credit = creditRaw == null || creditRaw.isBlank() ? null : Float.parseFloat(creditRaw);
                Integer maxCount = maxCountRaw == null || maxCountRaw.isBlank() ? null : Integer.parseInt(maxCountRaw);
                Integer year = Integer.parseInt(yearRaw);
                SemesterType semesterType = mapSemesterType(semesterRaw);
                TeachType teachType = mapTeachType(typeRaw);
                Integer targetGrade = mapGrade(gradeRaw);

                // 1) Course 찾기 or 생성 (name + majorType + credit 기준으로 키)
                String courseKey = name + "|" + majorType + "|" + credit;
                Course course = courseCache.get(courseKey);
                if (course == null) {
                    course = Course.builder()
                            .name(name)
                            .majority(majorType)
                            .time(time)
                            .credit(credit)
                            .build();
                    course = courseRepository.save(course);
                    courseCache.put(courseKey, course);
                }

                // 2) CourseType
                if (secondMajorRaw != null && !secondMajorRaw.isBlank()) {
                    SecondMajorType secondMajorType = mapSecondMajorType(secondMajorRaw.trim());
                    if (secondMajorType != SecondMajorType.NONE) {
                        CourseType courseType = CourseType.builder()
                                .course(course)
                                .secondMajor(secondMajorType)
                                .build();
                        courseTypeRepository.save(courseType);
                    }
                }

                // 3) Professor 찾기/생성
                Professor professor = professorRepository
                        .findByProfessorName(professorName)
                        .orElseGet(() -> professorRepository.save(
                                Professor.builder()
                                        .professorName(professorName)
                                        .build()
                        ));

                // 4) Teach 생성
                Teach teach = Teach.builder()
                        .course(course)
                        .professor(professor)
                        .year(year)
                        .semester(semesterType)
                        .number(Integer.parseInt(numberRaw))
                        .className(className == null ? null : className.trim())
                        .targetGrade(targetGrade)
                        .type(teachType)
                        .maxCount(maxCount)
                        .enrolledCount(0)
                        .remainCount(maxCount)
                        .build();
                teach = teachRepository.save(teach);

                // 5) TeachInfo 생성 (시간표 파싱)
                if (scheduleRaw != null && !scheduleRaw.isBlank()) {
                    List<TeachInfo> infos = parseSchedule(scheduleRaw, teach);
                    teachInfoRepository.saveAll(infos);
                }
            }
        }
    }


    private MajorType mapMajorType(String raw) {
        if (raw == null) return null;
        raw = raw.trim();
        if (raw.startsWith("전필-소프트")) return MajorType.MAJOR_REQUIRED;
        if (raw.startsWith("전선-소프트")) return MajorType.MAJOR_ELECTIVE;
        if (raw.startsWith("전기-소프트")) return MajorType.MAJOR_FOUNDATION;
        throw new IllegalArgumentException("Unknown majority: " + raw);
    }

    private SecondMajorType mapSecondMajorType(String raw) {
        if (raw == null || raw.isBlank()) return SecondMajorType.NONE;
        raw = raw.trim();
        if (raw.startsWith("복필-소프트")) return SecondMajorType.DOUBLE_MAJOR_REQUIRED;
        if (raw.startsWith("복선-소프트")) return SecondMajorType.DOUBLE_MAJOR_ELECTIVE;
        return SecondMajorType.NONE;
    }

    private SemesterType mapSemesterType(String raw) {
        if (raw == null) return null;
        raw = raw.trim();
        if (raw.equals("1학기")) return SemesterType.FIRST;
        if (raw.equals("2학기")) return SemesterType.SECOND;
        if (raw.contains("하계")) return SemesterType.SUMMER;
        if (raw.contains("동계")) return SemesterType.WINTER;
        throw new IllegalArgumentException("Unknown semester: " + raw);
    }

    private TeachType mapTeachType(String raw) {
        if (raw == null || raw.isBlank()) {
            return TeachType.KOREAN; // 기본 한국어
        }
        raw = raw.trim();
        if (raw.startsWith("EL+")) return TeachType.ENGAGED_LEARNING;
        if (raw.startsWith("영어")) return TeachType.ENGLISH;
        if (raw.startsWith("영한혼합")) return TeachType.MIXED;
        return TeachType.NONE;
    }

    private Integer mapGrade(String raw) {
        if (raw == null || raw.isBlank()) return null;
        raw = raw.trim();

        // 전체 허용 -> 제한 없음
        if (raw.equals("전체")) return null;

        if (raw.endsWith("학년")) {
            raw = raw.substring(0, raw.length() - 2); // "1학년" -> "1"
        }

        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    // 화 목 16:30-17:45 (정보과학관 21304) 파싱
    private List<TeachInfo> parseSchedule(String scheduleRaw, Teach teach) {
        List<TeachInfo> result = new ArrayList<>();

        String[] lines = scheduleRaw.split("\\R"); // 줄 단위
        for (String line : lines) {
            line = line.trim();
            if (line.isBlank()) continue;

            // 화 목 16:30-17:45 (정보과학관 21304)
            // 1) 요일 토큰 뽑기
            List<DayOfWeekType> days = new ArrayList<>();
            String[] tokens = line.split(" ");
            int idx = 0;
            while (idx < tokens.length && isKoreanDay(tokens[idx])) {
                days.add(mapDayOfWeek(tokens[idx]));
                idx++;
            }

            // 2) 나머지 문자열에서 시간, 강의실 파싱
            String rest = line.substring(line.indexOf(tokens[idx])).trim();
            // 16:30-17:45 (정보과학관 21304)
            String timePart = rest.substring(0, rest.indexOf("(")).trim(); // "16:30-17:45"
            String roomPart = rest.substring(rest.indexOf("(") + 1, rest.lastIndexOf(")")).trim(); // "정보과학관 21304"

            String[] times = timePart.split("-");
            LocalTime start = LocalTime.parse(times[0].trim());
            LocalTime end = LocalTime.parse(times[1].trim());

            for (DayOfWeekType day : days) {
                TeachInfo info = TeachInfo.builder()
                        .teach(teach)
                        .dayOfTheWeek(day)
                        .startTime(start)
                        .endTime(end)
                        .classroom(roomPart)
                        .build();
                result.add(info);
            }
        }

        return result;
    }

    private boolean isKoreanDay(String token) {
        return token.startsWith("월") || token.startsWith("화") ||
                token.startsWith("수") || token.startsWith("목") ||
                token.startsWith("금") || token.startsWith("토") ||
                token.startsWith("일");
    }

    private DayOfWeekType mapDayOfWeek(String token) {
        if (token.startsWith("월")) return DayOfWeekType.MONDAY;
        if (token.startsWith("화")) return DayOfWeekType.TUESDAY;
        if (token.startsWith("수")) return DayOfWeekType.WEDNESDAY;
        if (token.startsWith("목")) return DayOfWeekType.THURSDAY;
        if (token.startsWith("금")) return DayOfWeekType.FRIDAY;
        if (token.startsWith("토")) return DayOfWeekType.SATURDAY;
        if (token.startsWith("일")) return DayOfWeekType.SUNDAY;
        throw new IllegalArgumentException("Unknown day: " + token);
    }

}
