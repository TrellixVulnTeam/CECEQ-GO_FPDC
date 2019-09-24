from django.db import models

# Los demas datos los vamos a jalar de la base de datos de CECEQ

class usuarios(models.Model):
    user_id = models.IntegerField() # Con este id se relaciona la id de la base del ceceq
    estado = models.CharField(max_length=1) # Es un booleano
    fecha_inicio = models.DateField(auto_now_add=True) # Para saber cuando inicio en nuestro sistema

