# import firebase_admin
# from firebase_admin import credentials
# from google.cloud import firestore
# from firebase_admin import db
#
# cred = credentials.Certificate("C://Users/Raffi/Desktop/hackmit-2018-111f5-firebase-adminsdk-dddjd-b0861f8023.json")
# firebase_admin.initialize_app(cred)
# #db = firestore.Client('hackmit-2018-111f5')
#
# item = db.reference('images/{7EX7IR0OA6G3BJyxme1T}'.format()).get()
#
# #doc_ref = db.collection('images').document('7EX7IR0OA6G3BJyxme1T').get()
# #d = db.collection('images/dog'.format(new_user.key)).get()
# #print(doc_ref.doc_ref['name'])
import pyrebase
import io
import sys
from PyQt5 import QtGui

class Window(QtGui.QMainWindow):
    def __init__(self):
        super(Window,self).__init__()
        self.setGeometry(50,50,500,300)
        self.SetWindowTitle("Choose Image")
        self.show()


if __name__ == '__main__':

    app = QApplication(sys.argv)
    ex = Example()
    sys.exit(app.exec_())


def readFile(filePath):
    config = {
      "apiKey": "AIzaSyCItxeR8T5VCDkrbeKj4v8yrTXeqj3AC-k",
      "authDomain": "hackmit-2018-111f5.firebaseapp.com",
      "databaseURL": "https://hackmit-2018-111f5.firebaseio.com",
      "storageBucket": "hackmit-2018-111f5.appspot.com",
      "serviceAccount": "./hackmit-2018-111f5-2b1009a22c4b.json"
    }
    firebase = pyrebase.initialize_app(config)

    db = firebase.database()

    with open(filePath,'rb') as img:
        imgdata = img.read()

    img1 = {"name": "img1", "url": "google.com"}
    db.child("images").child("img1").set(img1)

if __name__ == '__main__':
    initUI()
