# opencv-java-face-recognition

0. install opencv with opencv-contrib

  version: 3.4.1

1. collect images from camera into folder imagedb/

  face.recognize.CollectData

  VM: -Djava.library.path=<opencv path>/build/lib

2. train images to generate YML file under folder model/

  face.recognize.Train

  VM: -Djava.library.path=<opencv path>/build/lib

3. recognition face from camera by using YML file

  face.recognize.Recognition

  VM: -Djava.library.path=<opencv path>/build/lib