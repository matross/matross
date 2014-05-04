temp-file
======================================================

Low level task to create a temporary file on the target machine.

Runs ``mktemp $TEMPFILE`` and returns the created file to stdout.

Example
~~~~~~~

.. code-block:: clojure

   {:temp-dir "/my/other/tmp", :type :temp-file}
   
   ;; you can also use the with-temp-files conveneince macro
   (with-temp-files [tmp-a tmp-b] (use-temp-files tmp-a tmp-b))

Configuration
~~~~~~~~~~~~~

``:temp-dir``
  Remote temporary directory (must already exist) - default: ``/tmp``

Resources
~~~~~~~~~

- `Task Source`_

.. _Task Source: https://github.com/matross/matross/blob/master/plugins/matross/tasks/temp_file.clj
