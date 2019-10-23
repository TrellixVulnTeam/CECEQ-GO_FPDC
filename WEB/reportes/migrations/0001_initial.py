# Generated by Django 2.2.4 on 2019-10-23 00:56

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Espacios',
            fields=[
                ('id_espacio', models.AutoField(primary_key=True, serialize=False)),
                ('des_espacio', models.CharField(blank=True, max_length=70, null=True)),
                ('facilidades', models.CharField(blank=True, max_length=100, null=True)),
                ('dirubicacion', models.CharField(blank=True, max_length=200, null=True)),
                ('dirforoprincipal', models.CharField(blank=True, db_column='dirforoPrincipal', max_length=200, null=True)),
                ('nombre_completo', models.CharField(max_length=100)),
                ('ubicacion', models.CharField(max_length=100)),
                ('foto', models.TextField()),
            ],
            options={
                'verbose_name': 'usuario_externo',
                'db_table': 'espacios',
                'managed': False,
            },
        ),
        migrations.CreateModel(
            name='Eventos',
            fields=[
                ('id_evento', models.AutoField(primary_key=True, serialize=False)),
                ('nom_evento', models.CharField(blank=True, max_length=100, null=True)),
                ('id_entidad', models.IntegerField(blank=True, null=True)),
                ('tipo_estadistico', models.IntegerField(blank=True, null=True)),
                ('estatus', models.CharField(blank=True, db_column='Estatus', max_length=1, null=True)),
                ('observaciones', models.TextField(blank=True, null=True)),
                ('fecha_programacion', models.DateField(blank=True, null=True)),
                ('id_capturista', models.IntegerField(blank=True, null=True)),
                ('costo', models.FloatField(blank=True, null=True)),
            ],
            options={
                'verbose_name': 'usuario_externo',
                'db_table': 'eventos',
                'managed': False,
            },
        ),
    ]