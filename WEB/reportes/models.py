from django.db import models

# Create your models here.
class Espacios(models.Model):
    class Meta:
        managed = False
        db_table = 'espacios'

    id_espacio = models.AutoField(primary_key=True)
    des_espacio = models.CharField(max_length=70, blank=True, null=True)
    facilidades = models.CharField(max_length=100, blank=True, null=True)
    dirubicacion = models.CharField(max_length=200, blank=True, null=True)
    dirforoprincipal = models.CharField(db_column='dirforoPrincipal', max_length=200, blank=True, null=True)  # Field name made lowercase.
    nombre_completo = models.CharField(max_length=100)
    ubicacion = models.CharField(max_length=100)
    foto = models.TextField()


