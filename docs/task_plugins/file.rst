file
======================================================

Coerces files on the remote filesystem

Example
~~~~~~~

.. code-block:: clojure


Configuration
~~~~~~~~~~~~~

``:path``
  remote file path

``:group``
  file group

``:mode``
  unix file permissions

``:state``
  one of: ``:file, :directory, :absent`` - default: ``:file``

``:owner``
  file owner

Resources
~~~~~~~~~

- `Task Source`_

.. _Task Source: https://github.com/matross/matross/blob/master/plugins/matross/tasks/file.clj
