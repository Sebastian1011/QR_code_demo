#! /bin/bash
rm ./images/*.jpg
ffmpeg -i IMG_5471.MOV -r 20 images/%d.jpg
