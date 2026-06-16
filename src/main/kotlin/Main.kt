package org.iesra

import org.iesra.app.PhotoRenamerApp
import org.iesra.cli.ArgumentParser

fun main(args: Array<String>) {
    val inputPath = ArgumentParser().parse(args)
    PhotoRenamerApp().run(inputPath)
}
