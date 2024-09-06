set -e

mv target target2
git checkout publish
rm -r target
mv target2 target
git add .
git commit -m "$(date +%F_%H-%M-%S)"
git push
git checkout master