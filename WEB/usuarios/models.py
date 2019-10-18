from django.db import models
import datetime
# Los demas datos los vamos a jalar de la base de datos de CECEQ

class UsuariosRegistrados(models.Model):
    class Meta:
        verbose_name = "usuario"
    user_id = models.IntegerField(primary_key=True) # Con este id se relaciona la id de la base del ceceq
    estado = models.CharField(max_length=1, default = 'a') # Es un booleano, a - activado, d - desactivado
    fecha_inicio = models.DateField(auto_now_add=True) # Para saber cuando inicio en nuestro sistema
    last_login = models.DateField(default=datetime.date.today)

class Usuario(models.Model):
    class Meta:
        verbose_name = "usuario_externo"
        managed = False
        db_table = "usuario"
    id_usuario = models.IntegerField(primary_key=True)
    nombre_usuario = models.CharField(max_length=30)
    nombre = models.CharField(max_length=100)
    password = models.CharField(max_length=30)
    privilegios = models.CharField(max_length=32)
    status = models.CharField(max_length=20)