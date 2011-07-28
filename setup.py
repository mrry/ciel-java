from setuptools import setup
import subprocess
import glob
import os

CIEL_JAR = os.path.join("java-dist", "ciel-0.1.jar")
GSON_JAR = os.path.join("java-dist", "gson-1.7.1.jar")

built_jars = glob.glob("java-dist/*.jar")
if not CIEL_JAR in built_jars or not GSON_JAR in built_jars:
    try:
        subprocess.check_call("ant")
    except subprocess.CalledProcessError:
        import sys
        print >>sys.stderr, "Error building Java files."
        sys.exit(-1)

setup(
    name = "ciel-java",
    version = '0.1-dev',
    description = "Java bindings for the CIEL distributed execution engine",
    author = "Derek Murray",
    author_email = "Derek.Murray@cl.cam.ac.uk",
    url = "http://www.cl.cam.ac.uk/netos/ciel/",
    packages = [ 'cieljava' ],
    package_dir = { '' : 'src/python' },
    data_files = [ ("lib/ciel", ["java-dist/ciel-0.1.jar"]),
                   ("lib/ciel", ["java-dist/gson-1.7.1.jar"])],
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

