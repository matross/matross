command
======================================================

Execute a shell command on the target machine in a normalized environment.

   For example, `{:command &quot;echo $VAR&quot; :env {:VAR &quot;herp&quot;}}` is equivalent
   to running `/usr/bin/env -i VAR=herp /bin/sh -c 'echo $VAR'` on the target
   machine.

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
