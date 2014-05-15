stream-from-file
======================================================

Low level task to stream the contents of a file on the target machine
to the given writer locally.

Example
~~~~~~~

.. code-block:: clojure

   {:type :stream-from-file, :src "~/.bashrc", :dest my-input-stream}

Configuration
~~~~~~~~~~~~~

``:src``
  remote file path

``:dest``
  destination writer


Resources
~~~~~~~~~

- `Task Source`_

.. _Task Source: https://github.com/matross/matross/blob/master/plugins/matross/tasks/stream_from_file.clj
