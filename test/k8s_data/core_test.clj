(ns k8s-data.core-test
  (:require [clojure.test :refer :all]
            [k8s-data.core :as k8s-data]
            [schema.core :as s]))

#_(def PersistentVolumeSpec
    {:description "PersistentVolumeSpec is the specification of a persistent volume.",
     :type "object"
     :properties
     {:volumeMode {:description "volumeMode defines if a volume is intended to be used with a formatted filesystem or to remain in raw block state. Value of Filesystem is implied when not included in spec.",
                   :type "string"},
      :storageClassName {:description "Name of StorageClass to which this persistent volume belongs. Empty value means that this volume does not belong to any StorageClass.",
                         :type "string"},
      :photonPersistentDisk {:$ref "#/definitions/io.k8s.api.core.v1.PhotonPersistentDiskVolumeSource",
                             :description "PhotonPersistentDisk represents a PhotonController persistent disk attached and mounted on kubelets host machine"},
      :glusterfs {:$ref "#/definitions/io.k8s.api.core.v1.GlusterfsPersistentVolumeSource",
                  :description "Glusterfs represents a Glusterfs volume that is attached to a host and exposed to the pod. Provisioned by an admin. More info: https://examples.k8s.io/volumes/glusterfs/README.md"},
      :persistentVolumeReclaimPolicy {:description "What happens to a persistent volume when released from its claim. Valid options are Retain (default for manually created PersistentVolumes), Delete (default for dynamically provisioned PersistentVolumes), and Recycle (deprecated). Recycle must be supported by the volume plugin underlying this PersistentVolume. More info: https://kubernetes.io/docs/concepts/storage/persistent-volumes#reclaiming",
                                      :type "string"},
      :storageos {:$ref "#/definitions/io.k8s.api.core.v1.StorageOSPersistentVolumeSource",
                  :description "StorageOS represents a StorageOS volume that is attached to the kubelet's host machine and mounted into the pod More info: https://examples.k8s.io/volumes/storageos/README.md"},
      :capacity {:additionalProperties {:$ref "#/definitions/io.k8s.apimachinery.pkg.api.resource.Quantity"},
                 :description "A description of the persistent volume's resources and capacity. More info: https://kubernetes.io/docs/concepts/storage/persistent-volumes#capacity",
                 :type "object"},
      :rbd {:$ref "#/definitions/io.k8s.api.core.v1.RBDPersistentVolumeSource",
            :description "RBD represents a Rados Block Device mount on the host that shares a pod's lifetime. More info: https://examples.k8s.io/volumes/rbd/README.md"},
      :nfs {:$ref "#/definitions/io.k8s.api.core.v1.NFSVolumeSource",
            :description "NFS represents an NFS mount on the host. Provisioned by an admin. More info: https://kubernetes.io/docs/concepts/storage/volumes#nfs"},
      :claimRef {:$ref "#/definitions/io.k8s.api.core.v1.ObjectReference",
                 :description "ClaimRef is part of a bi-directional binding between PersistentVolume and PersistentVolumeClaim. Expected to be non-nil when bound. claim.VolumeName is the authoritative bind between PV and PVC. More info: https://kubernetes.io/docs/concepts/storage/persistent-volumes#binding"},
      :flocker {:$ref "#/definitions/io.k8s.api.core.v1.FlockerVolumeSource",
                :description "Flocker represents a Flocker volume attached to a kubelet's host machine and exposed to the pod for its usage. This depends on the Flocker control service being running"},
      :awsElasticBlockStore {:$ref "#/definitions/io.k8s.api.core.v1.AWSElasticBlockStoreVolumeSource",
                             :description "AWSElasticBlockStore represents an AWS Disk resource that is attached to a kubelet's host machine and then exposed to the pod. More info: https://kubernetes.io/docs/concepts/storage/volumes#awselasticblockstore"},
      :hostPath {:$ref "#/definitions/io.k8s.api.core.v1.HostPathVolumeSource",
                 :description
                 "HostPath represents a directory on the host. Provisioned by a developer or tester. This is useful for single-node development and testing only! On-host storage is not supported in any way and WILL NOT WORK in a multi-node cluster. More info: https://kubernetes.io/docs/concepts/storage/volumes#hostpath"},
      :quobyte {:$ref "#/definitions/io.k8s.api.core.v1.QuobyteVolumeSource",
                :description "Quobyte represents a Quobyte mount on the host that shares a pod's lifetime"},
      :azureDisk {:$ref "#/definitions/io.k8s.api.core.v1.AzureDiskVolumeSource",
                  :description "AzureDisk represents an Azure Data Disk mount on the host and bind mount to the pod."},
      :iscsi {:$ref "#/definitions/io.k8s.api.core.v1.ISCSIPersistentVolumeSource",
              :description "ISCSI represents an ISCSI Disk resource that is attached to a kubelet's host machine and then exposed to the pod. Provisioned by an admin."},
      :flexVolume {:$ref "#/definitions/io.k8s.api.core.v1.FlexPersistentVolumeSource",
                   :description "FlexVolume represents a generic volume resource that is provisioned/attached using an exec based plugin."},
      :cinder {:$ref "#/definitions/io.k8s.api.core.v1.CinderPersistentVolumeSource",
               :description "Cinder represents a cinder volume attached and mounted on kubelets host machine. More info: https://examples.k8s.io/mysql-cinder-pd/README.md"},
      :csi {:$ref "#/definitions/io.k8s.api.core.v1.CSIPersistentVolumeSource",
            :description "CSI represents storage that is handled by an external CSI driver (Beta feature)."},
      :cephfs {:$ref "#/definitions/io.k8s.api.core.v1.CephFSPersistentVolumeSource",
               :description "CephFS represents a Ceph FS mount on the host that shares a pod's lifetime"},
      :accessModes {:description "AccessModes contains all ways the volume can be mounted. More info: https://kubernetes.io/docs/concepts/storage/persistent-volumes#access-modes",
                    :items {:type "string"},
                    :type "array"},
      :mountOptions {:description "A list of mount options, e.g. [\"ro\", \"soft\"]. Not validated - mount will simply fail if one is invalid. More info: https://kubernetes.io/docs/concepts/storage/persistent-volumes/#mount-options",
                     :items {:type "string"},
                     :type "array"},
      :portworxVolume {:$ref "#/definitions/io.k8s.api.core.v1.PortworxVolumeSource",
                       :description "PortworxVolume represents a portworx volume attached and mounted on kubelets host machine"},
      :azureFile {:$ref "#/definitions/io.k8s.api.core.v1.AzureFilePersistentVolumeSource",
                  :description "AzureFile represents an Azure File Service mount on the host and bind mount to the pod."},
      :vsphereVolume {:$ref "#/definitions/io.k8s.api.core.v1.VsphereVirtualDiskVolumeSource",
                      :description "VsphereVolume represents a vSphere volume attached and mounted on kubelets host machine"},
      :scaleIO {:$ref "#/definitions/io.k8s.api.core.v1.ScaleIOPersistentVolumeSource",
                :description "ScaleIO represents a ScaleIO persistent volume attached and mounted on Kubernetes nodes."},
      :local {:$ref "#/definitions/io.k8s.api.core.v1.LocalVolumeSource",
              :description "Local represents directly-attached storage with node affinity"},
      :gcePersistentDisk {:$ref "#/definitions/io.k8s.api.core.v1.GCEPersistentDiskVolumeSource",
                          :description "GCEPersistentDisk represents a GCE Disk resource that is attached to a kubelet's host machine and then exposed to the pod. Provisioned by an admin. More info: https://kubernetes.io/docs/concepts/storage/volumes#gcepersistentdisk"},
      :nodeAffinity {:$ref "#/definitions/io.k8s.api.core.v1.VolumeNodeAffinity",
                     :description "NodeAffinity defines constraints that limit what nodes this volume can be accessed from. This field influences the scheduling of pods that use this volume."},
      :fc {:$ref "#/definitions/io.k8s.api.core.v1.FCVolumeSource",
           :description "FC represents a Fibre Channel resource that is attached to a kubelet's host machine and then exposed to the pod."}}})

(def simple-property
  {:volumeMode {:description "volumeMode defines if a volume is intended to be used with a formatted filesystem or to remain in raw block state. Value of Filesystem is implied when not included in spec.",
                :type "string"}})

(def array-property
  {:accessModes {:description "AccessModes contains all ways the volume can be mounted. More info: https://kubernetes.io/docs/concepts/storage/persistent-volumes#access-modes",
                 :items {:type "string"},
                 :type "array"}})

(def ref-property
  {:azureFile {:$ref "#/definitions/io.k8s.api.core.v1.AzureFilePersistentVolumeSource",
               :description "AzureFile represents an Azure File Service mount on the host and bind mount to the pod."}})

(deftest property->schema
  (testing "It should convert a property to a schema"
    (is (= {:volumeMode {:schema `s/Str}}
           (k8s-data/api-property->schema (first simple-property))))
    (is (= {:accessModes {:schema [`s/Str]}}
           (k8s-data/api-property->schema (first array-property))))
    (is (= {:azureFile {:schema `k8s-data.io.k8s.api.core.v1/AzureFilePersistentVolumeSource}}
           (k8s-data/api-property->schema (first ref-property))))))

(deftest type->schema-test
  (testing "It should convert property type to schema"
    (is (= `s/Str
           (k8s-data/type->schema {:type "string"})))
    (is (= [`s/Str]
           (k8s-data/type->schema {:type "array" :items {:type "string"}})))))