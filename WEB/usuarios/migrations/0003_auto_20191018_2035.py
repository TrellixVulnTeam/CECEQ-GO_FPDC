# Generated by Django 2.2.4 on 2019-10-19 01:35

import datetime
from django.db import migrations, models
from django.utils.timezone import utc


class Migration(migrations.Migration):

    dependencies = [
        ('usuarios', '0002_auto_20191018_1858'),
    ]

    operations = [
        migrations.AlterField(
            model_name='usuariosregistrados',
            name='date_joined',
            field=models.DateTimeField(default=datetime.datetime(2019, 10, 19, 1, 35, 55, 542826, tzinfo=utc)),
        ),
        migrations.AlterField(
            model_name='usuariosregistrados',
            name='last_login',
            field=models.DateTimeField(default=datetime.datetime(2019, 10, 19, 1, 35, 55, 542826, tzinfo=utc)),
        ),
    ]
