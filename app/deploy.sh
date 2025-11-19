#!/bin/bash

echo ">>> Stopping service"
sudo systemctl stop shoppingcart || true

echo ">>> Starting service"
sudo systemctl start shoppingcart

echo ">>> Deployment completed"