package Dolphin.ShoppingCart.domain.course.entity;

import Dolphin.ShoppingCart.domain.model.entity.BaseEntity;
import Dolphin.ShoppingCart.domain.model.enums.DayOfWeekType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class TeachInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teach_id")
    private Teach teach;

    @Enumerated(EnumType.STRING)
    private DayOfWeekType dayOfTheWeek;

    private LocalTime startTime;
    private LocalTime endTime;
    private String classroom;
}