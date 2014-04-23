extensions = []

source_suffix = '.rst'
templates_path = ['.templates']
html_static_path = ['.static']
exclude_patterns = ['.build']

master_doc = 'index'
project = u'matross'
copyright = u'2014, Darrell Hamilton, Thomas Omans'

version = '0.1.0-SNAPSHOT'
release = '0.1.0-SNAPSHOT'

pygments_style = 'sphinx'
html_theme = 'default'

man_pages = [
    ('index', 'matross', u'matross Documentation',
     [u'Darrell Hamilton, Thomas Omans'], 1)
]
