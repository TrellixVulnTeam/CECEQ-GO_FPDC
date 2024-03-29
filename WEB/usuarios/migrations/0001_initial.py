# Generated by Django 2.2.4 on 2019-11-27 19:09

from django.db import migrations, models
import django.db.models.deletion
import django.utils.timezone


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Usuario',
            fields=[
                ('id_usuario', models.IntegerField(primary_key=True, serialize=False)),
                ('nombre_usuario', models.CharField(max_length=30, unique=True)),
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
            name='Permisos',
            fields=[
                ('id', models.IntegerField(primary_key=True, serialize=False)),
                ('nombre', models.CharField(max_length=50, unique=True)),
            ],
        ),
        migrations.CreateModel(
            name='Roles',
            fields=[
                ('id', models.IntegerField(primary_key=True, serialize=False)),
                ('nombre', models.CharField(max_length=50, unique=True)),
            ],
        ),
        migrations.CreateModel(
            name='UsuariosAnonimos',
            fields=[
                ('user_id', models.AutoField(primary_key=True, serialize=False)),
                ('numberofsessions', models.IntegerField(db_column='numberOfSessions')),
                ('time', models.DateTimeField()),
            ],
            options={
                'verbose_name': 'usuario',
            },
        ),
        migrations.CreateModel(
            name='UsuariosRegistrados',
            fields=[
                ('id', models.IntegerField(primary_key=True, serialize=False)),
                ('is_active', models.IntegerField(default=1)),
                ('date_joined', models.DateTimeField(default=django.utils.timezone.now)),
                ('last_login', models.DateTimeField(default=django.utils.timezone.now)),
                ('username', models.CharField(max_length=30, unique=True)),
                ('rol', models.ForeignKey(default=1, on_delete=django.db.models.deletion.CASCADE, to='usuarios.Roles')),
            ],
            options={
                'verbose_name': 'usuario',
            },
        ),
        migrations.CreateModel(
            name='PermisosRoles',
            fields=[
                ('id', models.IntegerField(primary_key=True, serialize=False)),
                ('id_permiso', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='usuarios.Permisos')),
                ('id_rol', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='usuarios.Roles')),
            ],
        ),
    ]
