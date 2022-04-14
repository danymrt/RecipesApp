from settings import *
import json

# Initializing our database
db = SQLAlchemy(app)

class User(db.Model):
    __bind_key__ = 'db1'
    __tablename__ = 'users'
    id = db.Column(db.String(128), primary_key = True, autoincrement=False)
    username = db.Column(db.String(128), nullable =False)
    name = db.Column(db.String(128))
    family_name = db.Column(db.String(128))
    email = db.Column(db.String(128),nullable=False)
    image = db.Column(db.String(1024))
    shopping = db.Column(db.JSON())

    # this method we are defining will convert our output to json
    def json(self):
        return {'id': self.id, 'username': self.username,'name': self.name,
        'family_name': self.family_name, 'email' : self.email, 'image':self.image, 'shopping': self.shopping}

    #function to add user to database
    def add_user(_id, _username, _name, _family_name, _email, _image, _shopping):
        # creating an instance of our User constructor
        new_user = User(id=_id, username=_username, name =_name, family_name =_family_name, email =_email, image = _image, shopping = _shopping)
        db.session.add(new_user)  # add new user to database session
        db.session.commit()  # commit changes to session

    #function to get all users in our database
    def get_all_users():
        return [User.json(user) for user in User.query.all()]

    #function to get user using the id as parameter
    def get_user(_id):
        return [User.json(User.query.filter_by(id=_id).first())]

    #function to update the details of a user using the id, and the information as parameters
    def update_user(_id, record):
        user = User.query.filter_by(id=_id).first()
        for column, value in record.items():
            if(column == "name"): user.name = value
            elif(column == "username"): user.username = value
            elif(column == "family_name"): user.family_name = value
            elif(column == "email"): user.email = value
            elif(column == "image"): user.image = value
            db.session.commit()

    #function to delete a user from our database using the id
    def delete_user(_id):
        User.query.filter_by(id=_id).delete()
        db.session.commit()


    #function that add element in the shopping list of the user
    def add_shopping(_id, _name, _image):
        user = User.query.filter_by(id=_id).first()
        element = Element(_name,_image)
        shop = user.shopping.copy()
        shop.append(element.ToJson())
        user.shopping = shop
        db.session.commit()

    #function that modify elements in the shopping list of the user
    def modify_shopping(_id, _name, record):
        user = User.query.filter_by(id=_id).first()
        shop = user.shopping.copy()
        img = record['image']

        for i,elem in enumerate(shop):
            print(elem)
            if elem['name'] == _name:
                element = Element(elem['name'],img)
                del shop[i]

        shop.append(element.ToJson())
        user.shopping = shop
        db.session.commit()

    #function that modify elements in the shopping list of the user
    #Body: {"list": ["apple","banana"]}
    def delete_shopping(_id, record):
        user = User.query.filter_by(id=_id).first()
        shop = user.shopping.copy()
        list_delete = record['list']
        for val in list_delete:
            for i,elem in enumerate(shop):
                if elem['name'] == val:
                    del shop[i]
        user.shopping = shop
        db.session.commit()

class Element:
    def __init__(self, name, image):
        self.name = name
        self.image = image

    def ToJson(self):
        return {'name': self.name, 'image': self.image}
