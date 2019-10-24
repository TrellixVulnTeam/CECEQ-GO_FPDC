from django.db import models
import datetime
from django.utils import timezone


# Los demas datos los vamos a jalar de la base de datos de CECEQ

class UsuariosRegistrados(models.Model):
    class Meta:
        verbose_name = "usuario"

    id = models.IntegerField(primary_key=True)  # Con este id se relaciona la id de la base del ceceq
    is_active = models.IntegerField(default=1)  # Es un entero donde 1 representa activo
    date_joined = models.DateTimeField(default=timezone.now)  # Para saber cuando inicio en nuestro sistema
    last_login = models.DateTimeField(default=timezone.now)
    username = models.CharField(max_length=30, unique=True)

    @property
    def is_authenticated(self):
        return True


class Usuario(models.Model):
    class Meta:
        verbose_name = "usuario_externo"
        managed = False
        db_table = "usuario"

    id_usuario = models.IntegerField(primary_key=True)
    nombre_usuario = models.CharField(max_length=30, unique=True)
    nombre = models.CharField(max_length=100)
    password = models.CharField(max_length=30)
    privilegios = models.CharField(max_length=32)
    status = models.CharField(max_length=20)


class UsuariosAnonimos(models.Model):
    class Meta:
        verbose_name = "usuario"
    user_id = models.AutoField(primary_key=True) # Con este id se relaciona la id de la base del ceceq
    estado = models.CharField(max_length=1, default = '1') # Es un booleano, a - activado, d - desactivado
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

class MobileSessions(models.Model):
    numberofsessions = models.IntegerField(db_column='numberOfSessions')  # Field name made lowercase.
    time = models.DateTimeField()

    class Meta:
        managed = False
        db_table = 'mobile_Sessions'


