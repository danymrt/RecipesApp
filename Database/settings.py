from flask import Flask, request, Response, jsonify
from flask_sqlalchemy import SQLAlchemy
from flask_restful import Resource, Api, reqparse

app = Flask(__name__)
app.config["DEBUG"] = True
api = Api(app)


#User Database
SQLALCHEMY_DATABASE1_URI = "mysql+mysqlconnector://{username}:{password}@{hostname}/username$DatabaseName".format(
    username="username",
    password="password",
    hostname="hostname",
)

#Favourite Recipe Database
SQLALCHEMY_DATABASE2_URI = "mysql+mysqlconnector://{username}:{password}@{hostname}/username$DatabaseName".format(
    username="username",
    password="password",
    hostname="hostname",
)

SQLALCHEMY_BINDS = {
    'db1': SQLALCHEMY_DATABASE1_URI,
    'db2': SQLALCHEMY_DATABASE2_URI
}

app.config["SQLALCHEMY_DATABASE_URI"] = SQLALCHEMY_DATABASE1_URI
app.config["SQLALCHEMY_BINDS"] = SQLALCHEMY_BINDS
app.config["SQLALCHEMY_POOL_RECYCLE"] = 299                         #it should throw away connections that haven’t been used for 299 seconds
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
