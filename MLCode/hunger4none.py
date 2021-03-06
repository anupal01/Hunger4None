# -*- coding: utf-8 -*-
"""Hunger4None.ipynb

Automatically generated by Colaboratory.

Original file is located at
    https://colab.research.google.com/drive/1Qi9tqUePy0IUFurHafWjjQDhFYKYlBSZ
"""

!pip install -q tflite-model-maker
# !pip install tensorflow

import os
import numpy as np
import tensorflow as tf
assert tf.__version__.startswith('2')
from tflite_model_maker import model_spec
from tflite_model_maker import image_classifier
from tflite_model_maker.config import ExportFormat
from tflite_model_maker.config import QuantizationConfig
from tflite_model_maker.image_classifier import DataLoader
from zipfile import ZipFile
import matplotlib.pyplot as plt
import concurrent.futures



# from google.colab import drive
# drive.mount('/content/drive')

plants = ['apple', 'cherry', 'corn', 
               'grape', 'orange_exclude', 'peach', 'pepper_bell', 'potato',
               'strawberry', 'tomato']

image_path = tf.keras.utils.get_file(
                                  'dataset2.zip',
                                  "file:///content/drive/MyDrive/dataset2.zip",
                                   extract=True)

_image_path = image_path

model_metrics_all = []
for plant in plants:
  model_metrics = []
  print(plant)
  image_path = _image_path
  image_path = os.path.join(os.path.dirname(image_path), 'dataset2/{}'.format(plant))
  print(image_path)
  data = DataLoader.from_folder(image_path)
  train_data, test_data = data.split(0.9)
  # validation_data, test_data = rest_data.split(0.5)

  model = image_classifier.create(
                                train_data, model_spec='efficientnet_lite0', validation_data=None,
                                  batch_size=128, epochs=10, train_whole_model=None, dropout_rate=None,
                                  learning_rate=0.03, momentum= True, shuffle=False, use_augmentation=False,
                                  use_hub_library=True, warmup_steps=None, model_dir=None, do_train=True
                                 )
  
  loss, accuracy = model.evaluate(test_data)
  model_metrics.append(plant)
  model_metrics.append(loss)
  model_metrics.append(accuracy)
  config = QuantizationConfig.for_float16()
  model_metrics_all.append(model_metrics)
  print(model_metrics_all)
  model.export(export_dir='/content/drive/MyDrive/Trained_models2/', tflite_filename='model_{}_fp16.tflite'.format(plant), quantization_config=config)

"""# Single class Training"""

data = DataLoader.from_folder(image_path)
train_data, rest_data = data.split(0.9)
validation_data, test_data = rest_data.split(0.5)

plt.figure(figsize=(10,10))
for i, (image, label) in enumerate(data.gen_dataset().unbatch().take(25)):
  plt.subplot(5,5,i+1)
  plt.xticks([])
  plt.yticks([])
  plt.grid(False)
  plt.imshow(image.numpy(), cmap=plt.cm.gray)
  plt.xlabel(data.index_to_label[label.numpy()])
plt.show()

model = image_classifier.create(
                                train_data, model_spec='efficientnet_lite0', validation_data=None,
                                  batch_size=None, epochs=None, train_whole_model=None, dropout_rate=None,
                                  learning_rate=None, momentum=None, shuffle=False, use_augmentation=False,
                                  use_hub_library=True, warmup_steps=None, model_dir=None, do_train=True
                              )
loss, accuracy = model.evaluate(test_data)

# A helper function that returns 'red'/'black' depending on if its two input
# parameter matches or not.
def get_label_color(val1, val2):
  if val1 == val2:
    return 'black'
  else:
    return 'red'

# Then plot 100 test images and their predicted labels.
# If a prediction result is different from the label provided label in "test"
# dataset, we will highlight it in red color.
plt.figure(figsize=(30, 30))
predicts = model.predict_top_k(test_data)
for i, (image, label) in enumerate(test_data.gen_dataset().unbatch().take(100)):
  ax = plt.subplot(15, 15, i+1)
  plt.xticks([])
  plt.yticks([])
  plt.grid(False)
  plt.imshow(image.numpy(), cmap=plt.cm.gray)

  predict_label = predicts[i][0][0]
  color = get_label_color(predict_label,
                          test_data.index_to_label[label.numpy()])
  ax.xaxis.label.set_color(color)
  plt.xlabel('P: %s' % predict_label)
plt.show()

config = QuantizationConfig.for_float16()
# model.export(export_dir='.', tflite_filename='model_fp16.tflite', quantization_config=config)

model.evaluate_tflite('model_fp16.tflite', test_data)