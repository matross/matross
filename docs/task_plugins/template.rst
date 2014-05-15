template
======================================================

Render the :file or :inline content as a mustache template and write the result to the :dest
file on the target machine.

Example
~~~~~~~

.. code-block:: clojure

   {:type :template, :dest "~/wow", :inline "{{ example }} is cool!", :vars {:example "matross"}}

Configuration
~~~~~~~~~~~~~

``:file``
  a local template file to render

``:dest``
  where to put the templated content

``:inline``
  string to render (optional, instead of file)

``:vars``
  for template substitution


Resources
~~~~~~~~~

- `Task Source`_

.. _Task Source: https://github.com/matross/matross/blob/master/plugins/matross/tasks/template.clj
