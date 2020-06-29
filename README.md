### Smart Phone으로 영상을 보면서 제어하는 Remote Control Car
<hr/>

사용 재료 : Raspberry Pi 2 B+, Servo Motor, ipTIME USB-AP(N100mini), 파이 카메라,  스마트 폰, 외장 배터리, 고장난 사촌 조카의 장난감 자동차 바퀴와 축, 빵판 등

[Circuit]</br>
PWM회로 구성 for RC Car의 변속 제어
Motor와 Raspberry Pi 전원 인가용 스위치

[RC Car SW]</br>
OS : Raspbian Jessi</br>
Language : C</br>
Library : BCM2835 for PWM, wiringPi for control GPIO, GStreamer for video stream</br>

[SmartPhone App]</br>
OS : Android</br>
Developer Tool : Android Studio</br>
Language : Java</br>
Library : Vitamio for viedo stream</br> 

RCCar에 무선 Wi-Fi 모듈을 사용해서 Wi-Fi로 스마트 폰에 연결</br>

<img src="https://user-images.githubusercontent.com/65689549/84568600-7b755c80-adbb-11ea-8893-7849989ea44c.png" width="500px" height="300px" title="px(픽셀) 크기 설정" alt="RubberDuck"></img><br/>

![20200613154507-16553bff09 gif-2-mp4 com](https://user-images.githubusercontent.com/65689549/84569176-72868a00-adbf-11ea-885a-a70421676a72.gif)


