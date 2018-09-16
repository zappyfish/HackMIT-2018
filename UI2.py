# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'C:\Users\Raffi\Desktop\ver2.ui'
#
# Created by: PyQt5 UI code generator 5.11.2
#
# WARNING! All changes made in this file will be lost!

from PyQt5 import QtCore, QtGui, QtWidgets,Qt
import pyrebase
import io, os
from livewiresegmentation import LiveWireSegmentation
from segment import Segmenter
import cv2
import numpy as np
import scipy.ndimage as nd






class Ui_MainWindow(object):
    def setupUi(self, MainWindow):

        MainWindow.setObjectName("MainWindow")
        MainWindow.resize(570, 351)

        self.centralwidget = QtWidgets.QWidget(MainWindow)
        self.centralwidget.setObjectName("centralwidget")

        self.selectImageBtn = QtWidgets.QPushButton(self.centralwidget)
        self.selectImageBtn.setGeometry(QtCore.QRect(30, 300, 93, 28))
        self.selectImageBtn.setObjectName("selectImageBtn")

        self.imageLabel = QtWidgets.QLabel(self.centralwidget)
        self.imageLabel.setGeometry(QtCore.QRect(20, 10, 531, 261))
        self.imageLabel.setFrameShape(QtWidgets.QFrame.Box)
        self.imageLabel.setText("")
        self.imageLabel.setObjectName("imageLabel")

        self.submitBtn = QtWidgets.QPushButton(self.centralwidget)
        self.submitBtn.setGeometry(QtCore.QRect(460, 300, 93, 28))
        self.submitBtn.setObjectName("submitBtn")

        self.thresholdDec = QtWidgets.QPushButton(self.centralwidget)
        self.thresholdDec.setGeometry(QtCore.QRect(150, 300, 31, 28))
        self.thresholdDec.setObjectName("thresholdDec")

        self.thresholdInc = QtWidgets.QPushButton(self.centralwidget)
        self.thresholdInc.setGeometry(QtCore.QRect(250, 300, 31, 28))
        self.thresholdInc.setObjectName("thresholdInc")

        self.thresholdVal = QtWidgets.QLineEdit(self.centralwidget)
        self.thresholdVal.setGeometry(QtCore.QRect(190, 300, 51, 31))
        self.thresholdVal.setObjectName("thresholdVal")
        self.thresholdVal.isReadOnly()
        self.thresholdVal.setAlignment(QtCore.Qt.AlignCenter)
        self.thresholdVal.setReadOnly(True)
        self.thresholdVal.setText("1")

        self.autoSegmentBtn = QtWidgets.QPushButton(self.centralwidget)
        self.autoSegmentBtn.setGeometry(QtCore.QRect(320, 300, 111, 28))
        self.autoSegmentBtn.setObjectName("autoSegmentBtn")

        MainWindow.setCentralWidget(self.centralwidget)

        self.filePath = ""
        #setting buttons to be disabled at the beginning
        self.submitBtn.setEnabled(False)
        self.thresholdDec.setEnabled(False)
        self.thresholdInc.setEnabled(False)
        self.thresholdVal.setEnabled(False)
        self.autoSegmentBtn.setEnabled(False)

        self.retranslateUi(MainWindow)
        QtCore.QMetaObject.connectSlotsByName(MainWindow)

        self.selectImageBtn.clicked.connect(self.setImage)
        self.thresholdInc.clicked.connect(self.increaseThreshold)
        self.thresholdDec.clicked.connect(self.decreaseThreshold)
        self.autoSegmentBtn.clicked.connect(self.autoSegment)
        #self.submitBtn.clicked.connect(self.uploadImage)

    def retranslateUi(self, MainWindow):
        _translate = QtCore.QCoreApplication.translate
        MainWindow.setWindowTitle(_translate("MainWindow", "MainWindow"))
        self.selectImageBtn.setText(_translate("MainWindow", "Select Image"))
        self.submitBtn.setText(_translate("MainWindow", "Submit"))
        self.thresholdDec.setText(_translate("MainWindow", "▼"))
        self.thresholdInc.setText(_translate("MainWindow", "▲"))
        self.autoSegmentBtn.setText(_translate("MainWindow", "Auto-Segment"))

    def setImage(self):
        filePath, _ = QtWidgets.QFileDialog.getOpenFileName(None, "Select Image", "", "Image Files (*.png *.jpg *.jpeg *.bmp)")
        if filePath:
            pixmap = QtGui.QPixmap(filePath)
            pixmap = pixmap.scaled(self.imageLabel.width(), self.imageLabel.height(), QtCore.Qt.KeepAspectRatio)
            self.imageLabel.setPixmap(pixmap)
            self.imageLabel.setAlignment(QtCore.Qt.AlignCenter)
            self.thresholdInc.setEnabled(True)
            self.thresholdDec.setEnabled(True)
            self.thresholdVal.setEnabled(True)
            self.autoSegmentBtn.setEnabled(True)
        self.filePath = filePath
        self.segmenter = Segmenter(filePath)

    def uploadImage(self):
        pass

    def increaseThreshold(self):
        val = (int)(self.thresholdVal.text())
        self.thresholdVal.setText(str(val + 1))
        val = val + 1
        self.segmenter.threshold_and_morph(val)

    def decreaseThreshold(self):
        val = (int)(self.thresholdVal.text())
        if val == 1:
            return
        self.thresholdVal.setText(str(val - 1))
        val = val - 1
        self.segmenter.threshold_and_morph(val)

    def autoSegment(self):
        self.segmenter.auto_segment((int)(self.thresholdVal.text()))

if __name__ == "__main__":
    import sys
    app = QtWidgets.QApplication(sys.argv)
    MainWindow = QtWidgets.QMainWindow()
    ui = Ui_MainWindow()
    ui.setupUi(MainWindow)
    MainWindow.show()
    sys.exit(app.exec_())
