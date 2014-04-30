Command
============

These are a thing we maybe have

Example
~~~~~~~

.. code-block:: clojure

   {:command "echo $message"
    :env {:message "hello, world!"}}


Configuration
~~~~~~~~~~~~~

``:command``

  command to run

``:env``

  map containing shell environment (optional)

``:shell``

  path to shell to use (optional, defaults to ``/bin/sh``)

Resources
~~~~~~~~~

- `Task Source`_

.. _Task Source: https://github.com/matross/matross/blob/master/plugins/matross/tasks/command.clj
