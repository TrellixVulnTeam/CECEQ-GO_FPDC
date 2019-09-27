from django.db import models

# Los demas datos los vamos a jalar de la base de datos de CECEQ

class usuarios_registrados(models.Model):
    class Meta:
        app_label = 'usuario'
    user_id = models.IntegerField() # Con este id se relaciona la id de la base del ceceq
    estado = models.CharField(max_length=1, default = 'a') # Es un booleano, a - activado, d - desactivado
    fecha_inicio = models.DateField(auto_now_add=True) # Para saber cuando inicio en nuestro sistema

class usuario(models.Model):
    class Meta:
        app_label = 'usuario_externo'
    id_usuario = models.IntegerField()
    nombre_usuario = models.CharField(max_length=30)
    nombre = models.CharField(max_length=100)
    password = models.CharField(max_length=30)
    privilegios = models.CharField(max_length=32)
    status = models.CharField(max_length=20)