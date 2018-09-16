import pyrebase
import os
import uuid

class Database:

    def __init__(self):
        config = {
          "apiKey": "AIzaSyCItxeR8T5VCDkrbeKj4v8yrTXeqj3AC-k",
          "authDomain": "hackmit-2018-111f5.firebaseapp.com",
          "databaseURL": "https://hackmit-2018-111f5.firebaseio.com",
          "projectId": "hackmit-2018-111f5",
          "storageBucket": "hackmit-2018-111f5.appspot.com",
          "messagingSenderId": "907790921058",
          "serviceAccount": "hackmit-2018-111f5-2b1009a22c4b.json"
        }

        firebase = pyrebase.initialize_app(config)
        # Get a reference to the database service
        self._db = firebase.database()

    def push_points(self, img_name, contours):
        fileName = os.path.basename(img_name)
        uid = uuid.uuid4().hex
        print(fileName)
        d = {}
        for i in range(len(contours)):
            d[i] = contours[i].tolist()
        self._db.child("Users").child(uid).child("Images").child(fileName).set(d)
