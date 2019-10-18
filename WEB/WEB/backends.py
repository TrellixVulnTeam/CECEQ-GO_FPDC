#my custom model from my outer db
from django.contrib.auth.backends import ModelBackend
from usuarios.models import Usuario
from usuarios.models import UsuariosRegistrados
from django.contrib.auth.hashers import *

class PersonalizedLoginBackend(ModelBackend):
    """
    Login con la base de datos personalizada

    Permite a los usuarios hacer login con los mismos credenciales
    que ya usan por default.

    """
    def authenticate(self, request, username=None, password=None, **kwars):
        try:
            user = Usuario.objects.get(nombre_usuario=username)
        except Usuario.DoesNotExist:
            return None
        if check_password(password, make_password(user.password)):
            try:
                return UsuariosRegistrados.objects.get(user_id=user.id_usuario)
            except UsuariosRegistrados.DoesNotExist:
                return None
        else:
            return None

#    def get_user(self, user_id):
#        """ Obtener objeto de usuario a partir del id. """
#        try:
#            return login_bd.Usuario.objects.get(id_usuario=user_id)
#        except login_bd.Usuario.DoesNotExist:
#            return None