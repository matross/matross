Writing Task Plugins
====================

Tasks are configurable operations that execute on machines. They are
provided a remote connection and a user-supplied config. With these
they can chain together any number of other internal tasks and remote
processes.

Defining tasks
~~~~~~~~~~~~~~

The ``deftask`` macro is a convenient way to implement the required
protocols and multimethods required to hook into the matross plugin system.

.. code-block:: clojure

   (deftask :my-task [conn config]
     ;; (run conn commands...)
     (task-result task-successful? changed? result-map))

This will register the task ``:my-task`` for use with matross. Your
task may take any configuration and return any result-map.

Using your task
~~~~~~~~~~~~~~~

.. code-block:: clojure

   {:type :my-task
    :my-arg "hello, world"}

This map will be provided instances of ``:my-task`` as ``config``.

==================
External Resources
==================

Check out the source code for futher information.

- `Task Interface`_
- `Command Task`__

.. _Task Interface: https://github.com/matross/matross/blob/master/src/matross/tasks/core.clj

__ https://github.com/matross/matross/blob/master/plugins/matross/tasks/command.clj
