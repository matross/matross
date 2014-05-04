stream-to-file
======================================================

Write the contents of the given source input to the desired file on the target machine.

Example
~~~~~~~

.. code-block:: clojure

   {:type :stream-to-file, :src "alias git=wow", :dest "~/.bash_aliases"}

Configuration
~~~~~~~~~~~~~

``:src``
  source input, string or stream

``:dest``
  remote filepath

Resources
~~~~~~~~~

- `Task Source`_

.. _Task Source: https://github.com/matross/matross/blob/master/plugins/matross/tasks/stream_to_file.clj
