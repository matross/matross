Template
============

The template module lets you template files or contents and put them
on the remote machine.

Example
~~~~~~~

.. code-block:: clojure

   {:type :template
    :content "{{ name }} is cool!"
    :vars {:name "matross"}
    :dest "~/wow"}

Configuration
~~~~~~~~~~~~~

``:dest``

  where to put the templated content

``:vars``

  for template substitution

``:file``

  a local template file to render

``:content``

  a string to render (optional, instead of ``:file``)

Resources
~~~~~~~~~

- `Task Source`_

.. _Task Source: https://github.com/matross/matross/blob/master/plugins/matross/tasks/template.clj

