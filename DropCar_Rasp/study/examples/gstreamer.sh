#!/bin/bash
clear
sudo raspivid -t 0 -h 240 -w 320 -fps 15 -b 50000000 -vf -hf -o - |
sudo gst-launch -v fdsrc ! h264parse ! gdppay ! tcpserversink host=192.168.0.15 port=5000 |
sudo ./test-launch "( tcpclientsrc host=127.0.0.1 port=5000 ! gdpdepay ! avdec_h264 ! rtph264pay name=pay0 pt=96 )"
