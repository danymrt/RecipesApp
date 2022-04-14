from settings import *
import json


# Initializing our database
db = SQLAlchemy(app)

class Recipe(db.Model):
    __bind_key__ = 'db2'
    __tablename__ = 'recipes'
    id = db.Column(db.String(128), primary_key = True)
    id_recipe = db.Column(db.String(128), nullable =False)
    id_user = db.Column(db.String(128), nullable =False)
    name = db.Column(db.String(128), nullable =False)
    img = db.Column(db.String(128), nullable =False)
    like = db.Column(db.String(128), nullable =False)
    token = db.Column(db.String(2048), nullable =False)

    # this method we are defining will convert our output to json
    def json(self):
        return {'id':self.id,'id_recipe': self.id_recipe, 'id_user' : self.id_user, 'name': self.name, 'img':self.img , 'like':self.like, 'token':self.token}

    #function to add recipe to database
    def add_recipeDB(_id,_id_recipe, _id_user, _name, _img, _like, _token):
        # creating an instance of our User constructor
        print(_id)
        print(_id_recipe)
        new_recipe = Recipe(id = _id, id_recipe=_id_recipe, id_user=_id_user, name=_name, img=_img, like=_like, token = _token)
        db.session.add(new_recipe)  # add new user to database session
        db.session.commit()  # commit changes to session

    #function to get all recipes in our database
    def get_all_recipes():
        return [Recipe.json(recipe) for recipe in Recipe.query.all()]

    #function to get recipes in our database for notification
    def get_recipes_notification(_id):
        return [Recipe.json(recipe) for recipe in Recipe.query.filter_by(id_recipe=_id)]

    #function to get recipes in our database for specific user
    def get_recipes_user(_id):
        return [Recipe.json(recipe) for recipe in Recipe.query.filter_by(id_user=_id)]

    #function to get user using the id as parameter
    def get_recipe(_id):
        return [Recipe.json(Recipe.query.filter_by(id=_id).first())]

    #function to modify the token
    def modify_token(_id, _token):
        recipe = Recipe.query.filter_by(id=_id).first()
        recipe.token = _token
        db.session.commit()

    #function to delete a recipe from our database using the id
    def delete_recipe(_id):
        Recipe.query.filter_by(id=_id).delete()
        db.session.commit()

