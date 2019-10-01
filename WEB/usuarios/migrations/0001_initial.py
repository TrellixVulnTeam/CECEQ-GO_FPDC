# Generated by Django 2.2.4 on 2019-09-29 00:06

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Usuario',
            fields=[
                ('id_usuario', models.IntegerField(primary_key=True, serialize=False)),
                ('nombre_usuario', models.CharField(max_length=30)),
                ('nombre', models.CharField(max_length=100)),
                ('password', models.CharField(max_length=30)),
                ('privilegios', models.CharField(max_length=32)),
                ('status', models.CharField(max_length=20)),
            ],
            options={
                'verbose_name': 'usuario_externo',
                'db_table': 'usuario',
                'managed': False,
            },
        ),
        migrations.CreateModel(
            name='UsuariosRegistrados',
            fields=[
                ('user_id', models.IntegerField(primary_key=True, serialize=False)),
                ('estado', models.CharField(default='a', max_length=1)),
                ('fecha_inicio', models.DateField(auto_now_add=True)),
            ],
            options={
                'verbose_name': 'usuario',
            },
        ),
    ]
