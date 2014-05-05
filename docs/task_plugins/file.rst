file
======================================================

Coerces files on the remote filesystem

Example
~~~~~~~

.. code-block:: clojure

   {:path "/tmp/sensitive-file", :type :file, :state :absent}
   {:path "/etc/db/required-directory", :group "db", :mode 750, :type :file, :state :directory, :owner "root"}

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
