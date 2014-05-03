command
======================================================

Run a shell command on a remote machine.

Example
~~~~~~~

.. code-block:: clojure

   {:command "echo $message", :env {:message "hello, world!"}}

Configuration
~~~~~~~~~~~~~

``:command``

  command to run

``:env``

  map containing shell environment (optional) - default: ``{}``

``:shell``

  path to shell to use (optional) - default: ``/bin/sh``

Resources
~~~~~~~~~

- `Task Source`_

.. _Task Source: https://github.com/matross/matross/blob/master/plugins/matross/tasks/command.clj
