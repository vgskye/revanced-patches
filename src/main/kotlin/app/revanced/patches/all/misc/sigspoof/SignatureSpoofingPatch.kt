package app.revanced.patches.all.misc.sigspoof
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patcher.patch.options.PatchOption.PatchExtensions.stringPatchOption
import app.revanced.util.getNode
import org.w3c.dom.Element


@Patch(
    name = "Spoof Signature",
    description = "Spoofs the app's signature using FAKE_PACKAGE_SIGNATURE. " +
            "This allows google apps like YouTube to be installed without mounting or GmsCore. " +
            "Requires the ROM to support signature spoofing.",
    use = false,
)
@Suppress("unused")
object SignatureSpoofingPatch : ResourcePatch() {
    private val signature by stringPatchOption(
        key = "signature",
        default =
            "30820252308201bb02044934987e300d06092a864886f70d010104050030" +
            "70310b3009060355040613025553310b3009060355040813024341311630" +
            "140603550407130d4d6f756e7461696e205669657731143012060355040a" +
            "130b476f6f676c652c20496e6331143012060355040b130b476f6f676c65" +
            "2c20496e633110300e06035504031307556e6b6e6f776e301e170d303831" +
            "3230323032303735385a170d3336303431393032303735385a3070310b30" +
            "09060355040613025553310b300906035504081302434131163014060355" +
            "0407130d4d6f756e7461696e205669657731143012060355040a130b476f" +
            "6f676c652c20496e6331143012060355040b130b476f6f676c652c20496e" +
            "633110300e06035504031307556e6b6e6f776e30819f300d06092a864886" +
            "f70d010101050003818d00308189028181009f48031990f9b14726384e04" +
            "53d18f8c0bbf8dc77b2504a4b1207c4c6c44babc00adc6610fa6b6ab2da8" +
            "0e33f2eef16b26a3f6b85b9afaca909ffbbeb3f4c94f7e8122a798e0eba7" +
            "5ced3dd229fa7365f41516415aa9c1617dd583ce19bae8a0bbd885fc17a9" +
            "b4bd2640805121aadb9377deb40013381418882ec52282fc580d02030100" +
            "01300d06092a864886f70d0101040500038181004086669ed631da4384dd" +
            "d061d226e073b98cc4b99df8b5e4be9e3cbe97501e83df1c6fa959c0ce60" +
            "5c4fd2ac6d1c84cede20476cbab19be8f2203aff7717ad652d8fcc890708" +
            "d1216da84457592649e0e9d3c4bb4cf58da19db1d4fc41bcb9584f64e65f" +
            "410d0529fd5b68838c141d0a9bd1db1191cb2a0df790ea0cb12db3a4",
        title = "Signature",
        description = "The signature, hex-encoded.",
        required = true,
    )
    override fun execute(context: ResourceContext) {
        context.document["AndroidManifest.xml"].use { document ->
            val manifestElement = document.getNode("manifest") as Element
            val permNode = document.createElement("uses-permission")
            permNode.setAttribute("android:name", "android.permission.FAKE_PACKAGE_SIGNATURE")
            manifestElement.appendChild(permNode)
            val applicationElement = document.getNode("application") as Element
            val sigNode = document.createElement("meta-data")
            sigNode.setAttribute("android:name", "fake-signature")
            sigNode.setAttribute("android:value", "@string/fake_signature")
            applicationElement.appendChild(sigNode)
        }
        context.document["res/values/strings.xml"].use { document ->
            val resourcesElement = document.getNode("resources") as Element
            val sigNode = document.createElement("string")
            sigNode.setAttribute("name", "fake_signature")
            sigNode.textContent = signature
            resourcesElement.appendChild(sigNode)
        }
    }
}