Stream to File
==============

A low level task for streaming files to the remote machine.

Example
~~~~~~~

.. code-block:: clojure

   {:type :stream-to-file
    :src my-input-stream
    :dest "~/remote-file"}

Configuration
~~~~~~~~~~~~~

``:src``

  input stream or string to write to the remote file

``:dest``

  remote filepath

Resources
~~~~~~~~~

- `Task Source`_

.. _Task Source: https://github.com/matross/matross/blob/master/plugins/matross/tasks/stream_to_file.clj
