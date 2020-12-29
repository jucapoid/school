#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import datetime, sys, time, socket, random


my_host = '127.0.0.1'
my_port = 65432        # The port used by the server
bufferSize = 4096  # valor recomendado na doc. do python
timeout = 10        #sends data periodically after 10secs


ID = 1
TYPE = "co2"
LOCATION = "Evora"
FIR_VER = 0.01
UNIT = "ppm"
READ_VALUE = random.randrange(0, 1000)



def handle(s):
    while True:
        time.sleep(timeout)
        buf = str(ID) + '\n' + str(time.strftime('%c')) + '\n' + str(READ_VALUE)  + '\n' + UNIT + '\n' + str(FIR_VER) + '\n'
        s.sendall(buf.encode())
        data = s.recv(bufferSize)
        print(data.decode())
        if "Error ID" in data.decode():
            print("give new id")
        continue

if __name__ == "__main__":
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((my_host, my_port))
    ID, TYPE, LOCATION, FIR_VER = sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4]
    buf = 'sensor\n ID:' + str(ID) + '\t Type:' + TYPE + '\t Location:' + LOCATION + '\t firmware_version:' + str(FIR_VER) + '\n'
    s.sendall(buf.encode())
    data = s.recv(bufferSize)
    print(data.decode())
    handle(s)