Touchpack_Calibrate
===================

Touchpack touchscreen calibrate apk



1. Kernel
===================
1.1. Enable USB HID support in kernel, first find .config in Android kernel source folder, and enable HID related flags :
CONFIG_HID_SUPPORT=y
CONFIG_HID=y
CONFIG_USB_HID=y
CONFIG_HID_PID=y
CONFIG_USB_HIDDEV=y
1.2. Run "make clean" then run "make" to build kernel image


2. Android
===================
2.1 Add idc file under /system/usr/idc
2.2 vim system/core/rootdir/ueventd.rc， add a line
  /dev/usb/hiddev* 0777 radio radio
  





