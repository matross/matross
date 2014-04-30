Vagrant
============

The vagrant connection allows you to run tasks against a vagrant box
over SSH. Uses ``vagrant ssh-config <box-name>`` to query information about the
vagrant box.

Example
~~~~~~~

.. code-block:: clojure

   {:type :vagrant
    :name "ubuntu-14.04"}

Configuration
~~~~~~~~~~~~~

 ``:name``

   Name of the vagrant box. Defaults to ``default`` (same as vagrant).

Resources
~~~~~~~~~

- `Connection Source`_

.. _Connection Source: https://github.com/matross/matross/blob/master/plugins/matross/connections/vagrant.clj
