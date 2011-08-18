from setuptools import setup

setup(
    name = "ciel-java",
    version = '0.1-dev',
    description = "Java bindings for the CIEL distributed execution engine",
    author = "Derek Murray",
    author_email = "Derek.Murray@cl.cam.ac.uk",
    url = "http://www.cl.cam.ac.uk/netos/ciel/",
    packages = [ 'cieljava' ],
    package_dir = { '' : 'src/python' },
    entry_points = {'ciel.executor.plugin':['load=cieljava:load']},
    classifiers = [
            'Development Status :: 3 - Alpha',
            'Intended Audience :: Developers',
            'Intended Audience :: Science/Research',
            'License :: OSI Approved :: ISC License (ISCL)',
            'Operating System :: POSIX',
            'Topic :: System :: Distributed Computing',
        ],
    requires=['ciel (>=0.1)']
)

