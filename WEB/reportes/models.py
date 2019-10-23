from django.db import models

# Create your models here.
class Espacios(models.Model):
    class Meta:
        verbose_name = "usuario_externo"
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

class Eventos(models.Model):
    class Meta:
        verbose_name = "usuario_externo"
        managed = False
        db_table = 'eventos'
    id_evento = models.AutoField(primary_key=True)
    nom_evento = models.CharField(max_length=100, blank=True, null=True)
    id_entidad = models.IntegerField(blank=True, null=True)
    tipo_estadistico = models.IntegerField(blank=True, null=True)
    estatus = models.CharField(db_column='Estatus', max_length=1, blank=True, null=True)  # Field name made lowercase.
    observaciones = models.TextField(blank=True, null=True)
    fecha_programacion = models.DateField(blank=True, null=True)
    id_capturista = models.IntegerField(blank=True, null=True)
    costo = models.FloatField(blank=True, null=True)




