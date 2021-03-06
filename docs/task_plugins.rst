Task Plugins
============

Tasks are matross' main unit of coordination across
:doc:`connections <connection_plugins>`. Using tasks you can execute
specific commands over a remote connection and ensure the system is
in a desired state.

The core set of task plugins include:

.. toctree::
   :maxdepth: 1

   task_plugins/command
   task_plugins/script
   task_plugins/template
   task_plugins/file
   task_plugins/temp_file
   task_plugins/stream_to_file
   task_plugins/stream_from_file
