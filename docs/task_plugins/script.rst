script
======================================================

Execute a script :file or :inline content on the target machine,
optionally (by default) rendering it as a mustache template prior to execution.

See also ``matross.tasks.command``

Example
~~~~~~~

.. code-block:: clojure

   {:file "hello.sh.mustache", :type :script, :vars {:cool "script, yo"}}

Configuration
~~~~~~~~~~~~~

``:file``
  a file to render

``:template``
  whether or not to template the script - default: ``true``

``:env``
  environment variables to expose to the script - default: ``{}``

``:inline``
  a string to render (optional, instead of ``:file``)

``:vars``
  map of vars for templating (optional) - default: ``{}``


Resources
~~~~~~~~~

- `Task Source`_

.. _Task Source: https://github.com/matross/matross/blob/master/plugins/matross/tasks/script.clj
