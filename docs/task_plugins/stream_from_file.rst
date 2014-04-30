Stream from File
================

A low level task for streaming files from the remote machine.

Example
~~~~~~~

.. code-block:: clojure

   {:type :stream-to-file
    :src "~/remote-file"
    :dest my-input-stream}

Configuration
~~~~~~~~~~~~~

``:src``

  remote filepath

``:dest``

  input stream to write the remote file contents into

Resources
~~~~~~~~~

- `Task Source`_

.. _Task Source: https://github.com/matross/matross/blob/master/plugins/matross/tasks/stream_from_file.clj
