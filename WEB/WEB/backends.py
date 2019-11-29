#my custom model from my outer db
from django.contrib.auth.backends import ModelBackend
from usuarios.models import Usuario
from usuarios.models import UsuariosRegistrados
from django.contrib.auth.hashers import *
from login.util import *

class PersonalizedLoginBackend(ModelBackend):
    """
    Login con la base de datos personalizada

    Permite a los usuarios hacer login con los mismos credenciales
    que ya usan por default.

    """
    def authenticate(self, request=None, username=None, password=None, **kwars):
        try:
            user = Usuario.objects.get(nombre_usuario=username)
        except Usuario.DoesNotExist:
            set_error_login(request, 1)
            return None
        if check_password(password, make_password(user.password)):
            try:
                user = UsuariosRegistrados.objects.get(id=user.id_usuario)
            except UsuariosRegistrados.DoesNotExist:
                set_error_login(request, 2)
                return None
            if user.is_active == 1:
                set_error_login(request, 0)
                return user
            else:
                set_error_login(request, 3)
                return None
        else:
            set_error_login(request, 1)
            return None

    def get_user(self, user_id):
        """ Obtener objeto de usuario a partir del id. """
        from django.contrib.auth.models import AnonymousUser
        try:
            user = UsuariosRegistrados.objects.get(id=user_id)
        except Exception as e:
            user = AnonymousUser()
        return user



