# Generated by Django 2.2.4 on 2019-10-22 00:42

from django.db import migrations, models
import django.utils.timezone


class Migration(migrations.Migration):

    dependencies = [
        ('usuarios', '0002_usuariosanonimos'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='usuariosanonimos',
            name='fecha_inicio',
        ),
        migrations.AddField(
            model_name='usuariosanonimos',
            name='created_at',
            field=models.DateTimeField(auto_now_add=True, default=django.utils.timezone.now),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='usuariosanonimos',
            name='updated_at',
            field=models.DateTimeField(auto_now=True),
        ),
        migrations.AlterField(
            model_name='usuariosanonimos',
            name='user_id',
            field=models.AutoField(primary_key=True, serialize=False),
        ),
    ]
