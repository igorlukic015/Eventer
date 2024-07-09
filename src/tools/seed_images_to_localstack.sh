#!/bin/bash

BUCKET_NAME=eventer-images
IMAGE_FOLDER=../services/admin/src/main/resources/seed_images/

awslocal s3api create-bucket --bucket $BUCKET_NAME --region us-east-1

for image in $IMAGE_FOLDER/*; do
    if [[ -f $image ]]; then
        awslocal s3 cp "$image" s3://$BUCKET_NAME/
    fi
done
