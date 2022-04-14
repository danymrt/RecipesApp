from user import *
from recipe import *

parser = reqparse.RequestParser()

@app.route('/notification/<string:id>', methods=['GET'])
def get_notification(id):
    # route to get all users
    if request.method == "GET":
        return jsonify({'Recipes': Recipe.get_recipes_notification(id)})

# route to get recipe by id
@app.route('/recipes/user/<string:id>', methods=['GET'])
def user_recipesFavourite(id):
    # route to get recipe by id
    if request.method == "GET":
        if(bool(User.query.filter_by(id=id).first())):
            return jsonify({'Recipes': Recipe.get_recipes_user(id)})
        else:
            return Response("User not in the database", 409, mimetype='application/json')

@app.route('/users', methods=['GET','POST'])
def load_users():
    # route to get all users
    if request.method == "GET":
        return jsonify({'Users': User.get_all_users()})

    # route to add new user
    if request.method == "POST":
        record = json.loads(request.data)
        if(bool(User.query.filter_by(id=record['id']).first())):
            response = jsonify(User.get_user(record['id']))
            #response = Response("User not in the database", 409, mimetype='application/json')
        else:
            User.add_user(record['id'],record['username'],
            record['name'],record['family_name'],record['email'],record['image'],[])
            response = jsonify(User.get_user(record['id']))
            #response = Response("User added", 201, mimetype='application/json')
        return response

# route to get user by id
@app.route('/users/<string:id>', methods=['GET','PUT','DELETE','POST'])
def user_by_id(id):
    # route to get user by id
    if request.method == "GET":
        if(bool(User.query.filter_by(id=id).first())==False):
            return Response("User Not Present in DataBase", status=404, mimetype='application/json')
        return_value = User.get_user(id)
        return jsonify(return_value)

    # route to update user with PUT method
    if request.method == "PUT":
        record = json.loads(request.data)
        if(bool(User.query.filter_by(id=id).first())):
            User.update_user(id, record)
            response = Response("User Updated", status=200, mimetype='application/json')
        else:
            response = Response("User Not Present in DataBase", status=404, mimetype='application/json')

        return response

    # route to delete user using the DELETE method
    if request.method == "DELETE":
        if(bool(User.query.filter_by(id=id).first())):
            User.delete_user(id)
            response = Response("User Deleted", status=200, mimetype='application/json')
        else:
            response = Response("User Not Present in DataBase", status=404, mimetype='application/json')
        return response

    # route to add new elem
    if request.method == "POST":
        record = json.loads(request.data)
        user = User.query.filter_by(id=id).first()
        for elem in user.shopping:
            if elem['name'] == record['name']:
                response = Response("Element already Present in DataBase", status=404, mimetype='application/json')
                return response
        User.add_shopping(id,record['name'],record['image'])
        response = Response("Element added", 201, mimetype='application/json')
        return response

# route to get user by id
@app.route('/users/<string:id>/shopping', methods=['GET','PUT','DELETE'])
def shopping_list(id):
    # route to get user by id
    if request.method == "GET":
        if(bool(User.query.filter_by(id=id).first())==False):
            return Response("User Not Present in DataBase", status=404, mimetype='application/json')
        return_value = User.get_user(id)
        print(type(return_value))
        return jsonify(return_value[0]['shopping'])

    # route to update shopping list with PUT method
    if request.method == "PUT":
        record = json.loads(request.data)
        if(bool(User.query.filter_by(id=id).first())):
            User.modify_shopping(id, record['name'], record)
            response = Response("Element Updated", status=200, mimetype='application/json')
        else:
            response = Response("User Not Present in DataBase", status=404, mimetype='application/json')
        return response

    # route to delete user using the DELETE method
    if request.method == "DELETE":
        record = json.loads(request.data)
        if(bool(User.query.filter_by(id=id).first())):
            User.delete_shopping(id,record)
            response = Response("Element Deleted", status=200, mimetype='application/json')
        else:
            response = Response("User Not Present in DataBase", status=404, mimetype='application/json')
        return response

@app.route('/recipes', methods=['GET','POST'])
def load_recipes():
    # route to get all recipes
    if request.method == "GET":
        return jsonify({'Recipes': Recipe.get_all_recipes()})

    # route to add new recipe
    if request.method == "POST":
        record = json.loads(request.data)
        print(record)
        if(bool(Recipe.query.filter_by(id=record['id']).first())):
            print("ok1")
            response = Response("Recipe already in the database", 201, mimetype='application/json')
        else:
            print("ok2")
            Recipe.add_recipeDB(record['id'],record['id_recipe'],record['id_user'], record['name'], record['img'], record['like'],record['token'])
            response = Response("Recipe added", 201, mimetype='application/json')
        return response


# route to get recipe by id
@app.route('/recipes/<string:id>', methods=['GET','DELETE', 'PUT'])
def recipe_by_id(id):
    # route to get recipe by id
    if request.method == "GET":
        if(bool(Recipe.query.filter_by(id=id).first())==False):
            return Response("Recipe Not Present in DataBase", status=404, mimetype='application/json')
        return_value = Recipe.get_recipe(id)
        return jsonify(return_value)

    # route to update shopping list with PUT method
    if request.method == "PUT":
        record = json.loads(request.data)
        if(bool(Recipe.query.filter_by(id=id).first())):
            Recipe.modify_token(id, record['token'])
            response = Response("Token Updated", status=200, mimetype='application/json')
        else:
            response = Response("Recipe Not Present in DataBase", status=404, mimetype='application/json')
        return response


    # route to delete recipe using the DELETE method
    if request.method == "DELETE":
        if(bool(Recipe.query.filter_by(id=id).first())):
            Recipe.delete_recipe(id)
            response = Response("Recipe Deleted", status=200, mimetype='application/json')
        else:
            response = Response("Recipe Not Present in DataBase", status=404, mimetype='application/json')
        return response

if __name__ == '__main__':
    app.run(debug=True)
