from usuarios.models import UsuariosAnonimos

def get_anonimus_users():
    users_r = UsuariosAnonimos.objects.all()
    list_of_ids = []
    number = 0
    for user_r in users_r:
        number = number + 1
        list_of_ids.append(user_r.user_id)
    return number
