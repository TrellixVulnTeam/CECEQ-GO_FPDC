from django.db import models

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
