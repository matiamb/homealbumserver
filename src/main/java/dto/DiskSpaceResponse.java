/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author matiambrosi
 */
public record DiskSpaceResponse(
        long totalSpaceBytes,
        long availableSpaceBytes,
        long usedSpaceBytes
        ) {}
